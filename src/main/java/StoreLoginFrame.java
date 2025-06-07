

/** Inheritance from abstract LoginBaseFrame class */

public class StoreLoginFrame extends LoginBaseFrame {
    public StoreLoginFrame() {
        super("Login - Store");
    }

    @Override
    protected String getIconPath() {
        return "/storeicon.png";
    }

    @Override
    protected void onLoginSuccess() {
        new StoreFrame();
    }
}
