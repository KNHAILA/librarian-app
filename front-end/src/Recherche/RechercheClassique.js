import React from 'react';
import Paper from '@material-ui/core/Paper';
import InputBase from '@material-ui/core/InputBase';
import IconButton from '@material-ui/core/IconButton';
import SearchIcon from '@material-ui/icons/Search';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import ListItem from '@material-ui/core/ListItem';
import { styled } from '@material-ui/core/styles';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import '../nested.css';
import { useEffect, useState } from "react";
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import ImageListItemBar from '@mui/material/ImageListItemBar';

const StyledTableCell = withStyles((theme) => ({
  head: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  body: {
    fontSize: 14,
  },
}))(TableCell);

const StyledTableRow = withStyles((theme) => ({
  root: {
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
  },
}))(TableRow);
const MyList = styled(ListItem)({
  background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
  border: 0,
  borderRadius: 3,
  boxShadow: '0 3px 5px 2px rgba(255, 105, 135, .3)',
  color: 'white',
  height: 48,
  padding: '0 30px',
});


const useStyles = makeStyles((theme) => ({
  root1: {
    padding: '2px 4px',
    display: 'flex',
    width: 400,
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    height: 28,
    margin: 4,
  },
  search: {
    display: 'flex',
    justifyContent: 'center',
    alignSelf: 'center',
    marginBottom: '50px'
  },
  coursSearch: {
    marginTop: '50px',
    marginLeft: '200px',
    marginRight: '200px'
  },
  root: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
  nested: {
    paddingLeft: theme.spacing(4),
  }
}));





export default function RechercheClassique() {
  const classes = useStyles();
  const [bookList, setBookList] = React.useState()
  const [motCle, setMotCle] = React.useState()
  const handleSearchClick = (motCle, e) => {
    e.preventDefault();
    console.log(`http://localhost:8080//classicSearch/list?name=${motCle}`);
    fetch(`http://localhost:8080//classicSearch/list?name=${motCle}`, {
      method: "GET",
    }).then(res => res.json())
      .then(data => {
        setBookList(data);
      })
  };

  return (
    <div className={classes.coursSearch} >
      <div className={classes.search}>
        <Paper component="form" className={classes.root1}>
          <InputBase
            className={classes.input}
            placeholder="Chercher un livre"
            inputProps={{ 'aria-label': 'Chercher un livre' }}
            value={motCle} onChange={e => setMotCle(e.target.value)}
          />
          <IconButton type="submit" className={classes.iconButton} aria-label="search" onClick={(e) => handleSearchClick(motCle, e)}>
            <SearchIcon />
          </IconButton>
        </Paper>
      </div>
      {
        bookList && bookList !== null ?
          <ImageList cols={4}>
            {bookList.map((item) => (
              <ImageListItem key={item.img}>
                <img
                  src={`${item.img}?w=248&fit=crop&auto=format`}
                  srcSet={`${item.img}?w=248&fit=crop&auto=format&dpr=2 2x`}
                  alt={item.title}
                  loading="lazy"
                />
                <ImageListItemBar className="bookInfo"
                  title={item.title}
                  subtitle={<span>by: {item.authors}</span>}
                  position="below"
                />
              </ImageListItem>
            ))}
          </ImageList> : ("")
      }
    </div>
  );
}
