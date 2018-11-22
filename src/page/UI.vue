<template>
  <div id="app">
     <button @click="show">show</button>
     <button @click="cancel">cancel</button>
  </div>
</template>

<script>
let lastId = 0

export default {
  name: "UI",
  methods: {
    show: function() {
      lastId = NativeAPI.Toast.show('Hello Yun App', Toast.LENGTH_SHORT)
    },
    cancel: function() {
      NativeAPI.Toast.cancel(lastId)
    },
  }
}
</script>

<style>
</style>
