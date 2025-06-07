

/** Inheritance from abstract LoginBaseFrame class */

public class ReportsLoginFrame extends LoginBaseFrame {
    public ReportsLoginFrame() {
        super("Login - Reports");
    }

    @Override
    protected String getIconPath() {
        return "/reportsicon.png";
    }

    @Override
    protected void onLoginSuccess() {
        new ReportsFrame();
    }
}
