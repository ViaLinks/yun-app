const path = require('path')

const VueLoaderPlugin = require('vue-loader/lib/plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const ZipPlugin = require('zip-webpack-plugin')

const rootDir = path.join(__dirname, '..')

module.exports = {
    mode: 'production',
    entry: path.join(rootDir, 'src', 'main.js'),
    output: {
        path: path.join(rootDir, 'dist'),
        filename: 'bundle.js',
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
        new ZipPlugin({
            filename: 'app.zip',
            exclude: [
                /\.map$/,
            ],
        }),
    ],
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            '@': path.join(rootDir, 'src'),
        }
    },
}