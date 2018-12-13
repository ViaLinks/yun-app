import {
    run
} from './utils'
const fs = require('fs')
const path = require("path")

// 调试期代码
let PAGES_PATH = path.resolve("src/pages")
let AIM_PATH = path.resolve("dist/pages")

fs.exists(AIM_PATH, function (exist) {
    if (!exist) {
        run(`mkdir -p ${AIM_PATH}`)
        fs.readdirSync(PAGES_PATH).forEach(function (ele, index) {
            if (!fs.statSync(PAGES_PATH + "/" + ele).isDirectory() && ele.indexOf('.html') > 0) {
                run(`cp ${PAGES_PATH}/${ele} ${AIM_PATH}`)
            }
        })
    }
})
