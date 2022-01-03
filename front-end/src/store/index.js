import Vue from 'vue'
import Vuex from 'vuex'
import { getLanguages, getBooksPerLanguage, getBooks } from '../service/books'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    languages: [],
    books: [],
  },
  mutations: {
    storeLanguages: (state, languages) => {
      state.languages = languages
    },
    storeBooks: (state, books) => {
      state.books = books
    },
  },
  actions: {
    fetchLanguages: async store => {
      const languages = await getLanguages()
      store.commit('storeLanguages', languages)
    },
    fetchBooks: async (store, lang) => {
      if (lang) store.commit('storeBooks', await getBooksPerLanguage(lang))
      else store.commit('storeBooks', await getBooks())
    },
  },
  modules: {},
  getters: {},
})
