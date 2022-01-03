import data from './database.json'

const getBooks = async () =>
  Object.values(data).reduce((acc, val) => acc.concat(val), [])

const getBooksPerLanguage = async language => data[language]

const getLanguages = async () => Object.keys(data)

export { getBooks, getBooksPerLanguage, getLanguages }
