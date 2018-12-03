const path = require('path')

const rootDir = path.join(__dirname, '..')
const srcDir = path.join(rootDir, 'src')
const distDir = path.join(rootDir, 'dist')

module.exports = {
    entry: path.join(srcDir, 'service', 'index.js'),
    output: {
        filename: 'service.js',
        path: distDir,
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            [
                                '@babel/preset-env',
                                {
                                    'targets': {
                                        'browsers': ['last 2 Chrome versions'],
                                    }
                                }
                            ],
                        ],
                        plugins: [
                            '@babel/plugin-proposal-class-properties',
                            '@babel/plugin-proposal-object-rest-spread',
                        ],
                    }
                }
            },
        ]
    },
    plugins: [
    ],
    resolve: {
        alias: {
            '@': srcDir,
        }
    },

    devtool: 'source-map',
    devServer: {
        contentBase: path.join(__dirname, 'build'),
        clientLogLevel: 'warning',
        compress: true,
        open: true,
    },
}