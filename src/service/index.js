import * as __appInfo__ from '@/../app.json'
import Bridge from '@/framework/Bridge'

// 通知 Native 'Service 启动成功'
Bridge.invoke('onServiceReady')