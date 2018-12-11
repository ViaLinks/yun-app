# Webview 的坑

### 为什么一定要加载 html 文件?

其中一个重要的原因是为了 chrome 调试. 只有加载 html 并在其中使用 javascript 标签, chrome inspector 才能在 Sources 中正确的识别源码文件, 方便我们使用断点调试.

另外, 别忘了 `webView.setWebContentsDebuggingEnabled(true)` 开启 Webview 调试, 否则你将在调试器里看到一片黑.