<template>
</template>

<script>
// 结合获取input
export default {
  name: 'ElementUtil',
  methods: {
    getValueByid: function (id) {
      return document.getElementById(id).value
    }
  }
}
</script>

<style scoped>

</style>
