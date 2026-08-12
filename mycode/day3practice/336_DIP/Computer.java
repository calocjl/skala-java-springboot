public class Computer {
    private final InputDevice inputDevice;
    private final OutputDevice outputDevice;

    public Computer(InputDevice inputDevice, OutputDevice outputDevice) {
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
    }

    public void operate() {
        inputDevice.type();
        outputDevice.display();
    }
}
