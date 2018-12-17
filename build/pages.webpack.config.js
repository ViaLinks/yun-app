const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const app = require('../app.json')

const rootDir = path.join(__dirname, '..')
const srcDir = path.join(rootDir, 'src')
const distDir = path.join(rootDir, 'dist', 'pages')

module.exports = {
    mode: 'production',
    entry: {
        index: path.join(srcDir, 'pages', 'index.js'),
        'toast': path.join(srcDir, 'pages', 'toast.js'),
    },
    output: {
        filename: '[name].js',
        path: distDir,
    },
    devtool: 'source-map',

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
        new HtmlWebpackPlugin({
            template: './src/pages/index.html',
            filename: 'index.html',
            inject: false,
        }),
        new HtmlWebpackPlugin({
            template: './src/pages/toast.html',
            filename: 'toast.html',
            inject: false,
        }),
    ],
    resolve: {
        extensions: ['.js', '.json'],
        alias: {
            '@': srcDir,
        }
    },
}