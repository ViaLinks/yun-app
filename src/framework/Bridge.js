import assert from 'assert-plus'

/**
 * Service JS 调用 Native 桥
 */
class Bridge {
    /**
     * key: callbackId
     * value: 回调函数
     * 
     * 回调函数参数:
     * @param {null | Error} err 是否失败
     * @param {String} data 回调的数据
     */
    callbacks = new Map()
    callbackIdStep = 1

    /**
     * Service JS 调用 Native 接口.
     * 所有调用都需要 Native 回调.
     * 
     * @param {String} api 调用的函数名
     * @param {Object} params 参数
     * @param {null | Array<String>} viewIds `null` 则为调用 Native API, 否则为调用相应 Page(View JS) 中的方法
     * @return {Promise<Object>}
     */
    invoke(api, params = {}, viewIds = null) {
        assert.string(api, '`api` must be a string')

        return new Promise((resolve, reject) => {
            const callbackId = this.getCallbackId(api)
            this.callbacks[callbackId] = function(err, data) {
                this.callbacks.delete(callbackId)

                if (err) {
                    reject(err)
                    return
                }
                resolve(JSON.parse(data))
            }

            if (viewIds) {
                // Servcie JS 调用 View JS (由 Native 转发)
                NativeAPI.invokeView(       // TODO: shenglian IMPL
                    api,
                    JSON.stringify(params),
                    callbackId,
                    viewIds,
                )
            } else {
                // Service JS 调用 Native API
                NativeAPI.invokeNative(     // TODO: shenglian IMPL
                    api,
                    JSON.stringify(params),
                    callbackId,
                )
            }
        })
    }

    getCallbackId(api) {
        return `${api}:${this.callbackId++}`
    }
}

export default new Bridge()