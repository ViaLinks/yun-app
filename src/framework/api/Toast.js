import Bridge from '../Bridge'

export default class Toast {
    static SHORT = 0
    static LONG = 1

    static show(text, duration = SHORT) {
        Bridge.invoke('Toast.show', [text, duration], null, false, true)
    }
}