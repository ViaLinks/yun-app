import PageApi from '@/framework/api/PageApi'
import Toast from '@/framework/api/Toast'

export default class ToastPageApi extends PageApi {
    show() {
        Toast.show('Hello, world!', Toast.SHORT)
    }
}
window.pageApi = new ToastPageApi()