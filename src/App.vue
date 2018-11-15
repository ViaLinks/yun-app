<template>
  <div id="app">
     <button @click="toast">Show Native Toast</button>
  </div>
</template>

<script>
export default {
  name: "App",
  methods: {
    toast: function() {
      const { Toast } = NativeAPI
      Toast.show('Hello Yun App', Toast.LENGTH_SHORT)
    }
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
