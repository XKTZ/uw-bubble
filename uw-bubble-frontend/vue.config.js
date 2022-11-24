const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  devServer: {
    proxy: {
      "/": {
        target: "http://192.168.43.53:8080",
        ws: false,
        changeOrigin: true
      }
    }
  }
})