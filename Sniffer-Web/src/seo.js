export default {
  name: 'SEO',
  methods: {
    baidu: function () {
      var bp = document.createElement('script')
      var curProtocol = window.location.protocol.split(':')[0]
      if (curProtocol === 'https') {
        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js'
      } else {
        bp.src = 'http://push.zhanzhang.baidu.com/push.js'
      }
      var s = document.getElementsByTagName('script')[0]
      s.parentNode.insertBefore(bp, s)
    },
    cnzz: function () {
      var oHead = document.getElementsByTagName('HEAD').item(0)
      var oScript = document.createElement('script')
      oScript.type = 'text/javascript'
      oScript.src = 'http://s11.cnzz.com/z_stat.php?id=1273316267&web_id=1273316267'
      oHead.appendChild(oScript)
    },
    start: function () {
      // this.baidu()
      this.cnzz()
    }
  }
}
