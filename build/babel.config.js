module.exports = {
    presets: [
        [
            "@babel/env",
            {
                targets: {
                    node: "current",
                },
                useBuiltIns: "usage"
            }
        ],
    ],
    plugins: [
    ],
    sourceMaps: true,
    retainLines: true,
}