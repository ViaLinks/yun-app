import PageApi from '@/framework/api/PageApi'

export default class IndexPageApi extends PageApi {
    helloWorld() {
        console.log('Hello, world!')
    }

    jump2SecondPage() {
        NativeApi.invokeView("goto", "{'name':'second'}", null, null)
    }
}
window.pageApi = new IndexPageApi()