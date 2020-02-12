<template>
  <div class="main">
    <img class="logo" src="../assets/logo.png">
      <h1>
        <input class="kw" name="wd"  maxlength="255" placeholder="搜点东西吧..." autocomplete="off"  v-model="wd"  @keydown="enterKeyDown($event)"  />
        <span class="s_btn_wr">
          <a  class="s_a"  v-on:click="search()" >找一下</a>
        </span>
      </h1>
  </div>
</template>

<script>
export default {
  name: 'MainSearch',
  methods: {
    search: function () {
      let route = {
        path: '/s/' + this.wd
      }
      this.$router.push(route)
    },
    enterKeyDown (ev) {
      if (ev.keyCode === 13) {
        this.search()
      }
    }
  },
  data () {
    return {
      wd: ''
    }
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  .main {
    overflow:hidden;
    font-family: 'Microsoft YaHei UI', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 200px;
  }

  .logo {
    width: 200px;
    height: 200px;
    border: none;
    text-decoration: none;
  }

  .s_btn_wr{
    display: inline-block;
    background-position: -120px -48px;
    z-index: 0;
    vertical-align: top;
    width: auto;
    height: auto;
    border-bottom: 1px solid transparent;

  }

  .s_a {
    cursor: pointer;
    width: 100px;
    color: #fff;
    font-size: 15px;
    letter-spacing: 1px;
    background: #3385ff;
    border-bottom: 1px solid #2d78f4;
    outline: medium;
    -webkit-appearance: none;
    -webkit-border-radius: 0;
    padding: 0;
    line-height: 30px;
    text-align: center;
    display: inline-block;
    text-decoration:none;
    height: 30px;
  }

.s_btn {
    width: 100px;
    height: 36px;
    color: #fff;
    font-size: 15px;
    letter-spacing: 1px;
    background: #3385ff;
    border-bottom: 1px solid #2d78f4;
    outline: medium;
    -webkit-appearance: none;
    -webkit-border-radius: 0;
    border: 0;
    padding: 0;
  }

  .kw {
    font: 16px/18px arial;
    width: 500px;
    height: 22px;
    line-height: 22px;
    margin: 6px 0 0 7px;
  }

</style>
