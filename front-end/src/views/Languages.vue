<template lang="html">
  <div class="languages">
    <div class="title">
      Choose a language
    </div>
    <div class="grid">
      <div class="language">
        <router-link to="lang/all">
          All
        </router-link>
      </div>
      <div
        class="language"
        v-for="lang in $store.state.languages"
        v-bind:key="lang"
      >
        <router-link :to="'lang/' + lang">
          {{ lang }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    this.$store.dispatch('fetchLanguages')
  },
}
</script>

<style lang="css" scoped>
.title {
  font-weight: 700;
  line-height: 1.3em;
  font-size: 2.3em;
  padding-bottom: 2rem;
  text-align: center;
}
.grid {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.language {
  padding: 2rem;
  margin: 2rem 0;
}

a {
  background: #fefefe;
  border-radius: 12px;
  padding: 2rem;
  line-height: 1em;
  font-size: 1.2em;
  text-decoration: none;
  color: black;
}

a:hover {
  background: #999;
}

a:active {
  background: #333;
}
</style>
