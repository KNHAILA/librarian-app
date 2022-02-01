import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import { Grid, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));

export default function NavBar(props) {
  const classes = useStyles();
    return(
      <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <Grid
            container
            direction="row"
            justify="space-around"
            alignItems="center"
          >
            <Button color="inherit">
            <Link to="/Accueil" className="link">Accueil</Link></Button>
            <Button color="inherit"><Link className="link" to="/RechercheClassique">Recherche Classique</Link></Button>
            <Button color="inherit"><Link className="link" to="/RechercheAvancee">Recherche Avancée</Link></Button>
         </Grid>
        </Toolbar>
      </AppBar>
    </div>
    )
}
