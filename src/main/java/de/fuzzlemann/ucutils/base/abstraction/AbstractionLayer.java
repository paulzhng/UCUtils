package de.fuzzlemann.ucutils.base.abstraction;

/**
 * @author Fuzzlemann
 */
public class AbstractionLayer {

    private static AbstractionLayer instance;

    public static AbstractionLayer getInstance() {
        if (instance == null) {
            instance = new AbstractionLayer();
        }

        return instance;
    }

    public static UPlayer getPlayer() {
        return getInstance().getAbstractPlayer();
    }

    private Class<? extends UPlayer> playerImplClass;
    private UPlayer player;

    private AbstractionLayer() {
        this.playerImplClass = UPlayerImpl.class;
    }

    public void setPlayerImplementation(Class<? extends UPlayer> playerImplClass) {
        if (this.playerImplClass != playerImplClass) {
            this.playerImplClass = playerImplClass;

            player = null; //force creation of new instance with the new implementation
        }
    }

    public UPlayer getAbstractPlayer() {
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
