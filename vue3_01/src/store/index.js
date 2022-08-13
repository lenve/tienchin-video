import { createStore } from 'vuex'

export default createStore({
  state: {
    name: 'javaboy'
  },
  getters: {
  },
  mutations: {
    SET_NAME(state, name) {
      state.name = name;
    }
  },
  actions: {
  },
  modules: {
  }
})
