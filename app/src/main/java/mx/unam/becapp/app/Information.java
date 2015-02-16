package mx.unam.becapp.app;

abstract class Information {
    protected Session session;

    protected String status;
    protected String message;

    abstract void getData();

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
