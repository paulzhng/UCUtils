package de.fuzzlemann.ucutils.utils.abstraction;

/**
 * @author Fuzzlemann
 */
public class AbstractionHandler {

    private static AbstractionHandler instance;

    public static AbstractionHandler getInstance() {
        if (instance == null) {
            instance = new AbstractionHandler();
        }

        return instance;
    }

    private Class<? extends UPlayer> playerImplClass;
    private UPlayer player;

    private AbstractionHandler() {
        this.playerImplClass = UPlayerImpl.class;
    }

    public void setPlayerImplementation(Class<? extends UPlayer> playerImplClass) {
        this.playerImplClass = playerImplClass;
    }

    public UPlayer getPlayer() {
        if (player == null) {
            try {
                this.player = playerImplClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        return player;
    }
}
