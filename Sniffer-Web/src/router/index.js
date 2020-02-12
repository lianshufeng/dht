import Vue from 'vue'
import Router from 'vue-router'
import MainSearch from '@/components/MainSearch'
import ListSearch from '@/components/ListSearch'

Vue.use(Router)

var router = new Router({
  routes: [
    {
      path: '/',
      name: 'MainSearch',
      component: MainSearch
    },
    {
      path: '/s/:wd',
      name: 'ListSearch',
      component: ListSearch
    },
    {
      path: '/s',
      name: 'ListSearch',
      component: ListSearch
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {
    from.name ? next({name: from.name}) : next('/')
  } else {
    next()
  }
})

export default router
