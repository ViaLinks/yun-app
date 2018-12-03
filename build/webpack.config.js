const path = require('path')

const VueLoaderPlugin = require('vue-loader/lib/plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const FileManagerPlugin = require('filemanager-webpack-plugin')

const app = require('../app.json')

const rootDir = path.join(__dirname, '..')
const srcDir = path.join(rootDir, 'src')
const distDir = path.join(rootDir, 'dist')
const targetZip = path.join(distDir, `${app.appId}.zip`)

module.exports = {
    mode: 'production',
    entry: {
        // view: path.join(srcDir, 'main.js'),
        service: path.join(srcDir, 'service', 'index.js')
    },
    output: {
        filename: '[name].js',
        path: distDir,
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                use: 'vue-loader',
            },
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
            {
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ]
            },
        ]
    },
    plugins: [
        new VueLoaderPlugin(),
        new HtmlWebpackPlugin({
            template: './src/index.html',
            inject: true,
        }),
        new FileManagerPlugin({
            onStart: [
                {
                    delete: [
                        path.join(distDir, '/*'),
                    ],
                },
            ],
            onEnd: [
                {
                    mkdir: [
                        distDir,
                    ],
                },
                {
                    copy: [
                        { source: path.join(rootDir, 'app.json'), destination: distDir, },
                    ],
                    archive: [
                        { source: path.join(distDir), destination: targetZip, },
                    ],
                },
            ],
        }),
    ],
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            '@': srcDir,
        }
    },
}