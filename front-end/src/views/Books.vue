<template lang="html">
  <div class="books">
    <div class="title">
      {{ $route.params.lang + ' books' }}
    </div>
    <div class="books-cards">
      <a
        target="_blank"
        rel="noopener noreferrer"
        :href="'https://www.gutenberg.org/ebooks/' + book.link"
        class="book"
        v-for="book in this.$store.state.books"
        v-bind:key="book.title"
      >
        <div class="book-content">
          <div>
            <div class="book-title">
              {{ book.Title }}
            </div>
            <div class="book-author">
              {{ book.Author }}
            </div>
          </div>
          <div class="book-cover">
            <img
              :src="
                `https://www.gutenberg.org/cache/epub/${book.link}/pg${book.link}.cover.medium.jpg`
              "
              alt=""
            />
          </div>
        </div>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    const lang = this.$route.params.lang
    if (lang === 'all') {
      this.$store.dispatch('fetchBooks')
    } else {
      this.$store.dispatch('fetchBooks', lang)
    }
  },
  data() {
    return {
      lang: this.$route.params.lang,
    }
  },
}
</script>

<style lang="css" scoped>
.books {
  padding: 0 4rem;
}
.title {
  font-weight: 700;
  line-height: 1.3em;
  font-size: 2.3em;
  padding-bottom: 4rem;
  text-align: center;
}

.book-title {
  grid-area: title;
  font-weight: 700;
}

.book-author {
  grid-area: author;
  font-style: italic;
}

.book-cover {
  grid-area: cover;
}
.book-cover img {
  height: 10rem;
}

.books-cards {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.book-content {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 20rem;
  background: white;
  padding: 2rem;
  margin: 1rem;
  border-radius: 12px;
}

.book {
  text-decoration: none;
  color: black;
}

@media only screen and (max-width: 800px) {
  .books {
    padding: 0;
  }
}
</style>
