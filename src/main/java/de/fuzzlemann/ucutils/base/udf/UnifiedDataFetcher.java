package de.fuzzlemann.ucutils.base.udf;

import com.mojang.authlib.exceptions.AuthenticationException;
import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.common.udf.*;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.io.JsonManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.io.File;
import java.util.*;

/**
 * @author Fuzzlemann
 */
public class UnifiedDataFetcher {

    private final File fallbackFile = new File(JsonManager.DIRECTORY, "udf.fallback");
    private final Map<String, UDFLoader<?>> namesToLoaders = new HashMap<>();
    private final Map<UDFModule, UDFLoader<?>> udfLoaders = new HashMap<>();

    public UnifiedDataFetcher(ASMDataTable asmDataTable) {
        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(UDFModule.class.getCanonicalName());
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());

                UDFLoader<?> dataLoader = (UDFLoader<?>) clazz.newInstance();
                UDFModule udfModule = ReflectionUtil.getAnnotation(clazz.getDeclaredAnnotations(), UDFModule.class);
                if (udfModule == null) throw new IllegalStateException("udf module not found");

                udfLoaders.put(udfModule, dataLoader);
                namesToLoaders.put(udfModule.value(), dataLoader);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException(e); //should not happen
            }
        }
    }

    public void reload() {
        for (UDFLoader<?> udfLoader : udfLoaders.values()) {
            udfLoader.cleanUp();
        }

        load();
    }

    public void load() {
        try {
            fetch();
        } catch (Exception e) {
            fallbackLoading();
        }
    }

    private void fetch() {
        DataCollection dataCollection = sendUDFRequest();
        save(dataCollection);
        distributeDataCollection(dataCollection);
    }

    private void fallbackLoading() {
        if (!fallbackFile.exists()) return;

        DataCollection dataCollection = JsonManager.loadObject(fallbackFile, DataCollection.class);
        distributeDataCollection(dataCollection);
    }

    private void save(DataCollection dataCollection) {
        JsonManager.writeObject(fallbackFile, dataCollection);
    }

    private DataCollection sendUDFRequest() {
        AuthHash authHash = getAuthHash();
        List<DataRequest> dataRequestList = new ArrayList<>();

        for (UDFModule udfModule : udfLoaders.keySet()) {
            DataRequest dataRequest = new DataRequest(udfModule.value(), udfModule.version());
            dataRequestList.add(dataRequest);
        }

        UDFRequest udfRequest = new UDFRequest(authHash, UCUtilsConfig.apiKey, dataRequestList);

        return APIUtils.post(DataCollection.class, "http://tomcat.fuzzlemann.de/factiononline/api/fetchData", "udfRequest", udfRequest);
    }

    private void distributeDataCollection(DataCollection dataCollection) {
        for (Data<?> data : dataCollection.getData()) {
            UDFLoader<?> udfLoader = namesToLoaders.get(data.getName());
            Class<?> clazz = ReflectionUtil.getGenericParameter(udfLoader.getClass(), 0, 0);
            Class<?> collectionParameterClass = null;

            if (clazz == null) {
                Map.Entry<Class<?>, Class<?>> collectionAndCollectionParameter = ReflectionUtil.getCollectionAndCollectionParameter(udfLoader.getClass(), 0, 0);
                if (collectionAndCollectionParameter == null) throw new IllegalStateException(); //should not happen

                clazz = collectionAndCollectionParameter.getKey();
                collectionParameterClass = collectionAndCollectionParameter.getValue();
            }

            Object content = data.getContent();
            if (content == null) continue;

            udfLoader.supplyO(content, clazz, collectionParameterClass);
        }
    }

    private AuthHash getAuthHash() {
        AuthHash authHash = new AuthHash(AbstractionLayer.getPlayer().getName());

        Minecraft mc = Main.MINECRAFT;
        if (ForgeUtils.isTest()) return null;

        try {
            mc.getSessionService().joinServer(mc.getSession().getProfile(), mc.getSession().getToken(), authHash.getHash());
            return authHash;
        } catch (AuthenticationException e) {
            Logger.LOGGER.catching(e);
            return null;
        }
    }

}
