const path = require('path')

const config = require('./webpack.config')
config.mode = 'development'
config.devtool = 'source-map'
config.devServer = {
    contentBase: path.join(__dirname, 'build'),
    clientLogLevel: 'warning',
    compress: true,
    open: true,
}

module.exports = config