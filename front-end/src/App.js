import './App.css';
import { BrowserRouter, Router, Route, Switch } from 'react-router-dom';
import RechercheClassique from './Recherche/RechercheClassique';
import RechercheAvancee from './Recherche/RechercheAvancee'
import Acceuil from './Accueil/Acceuil';
import NavBar from './NavBar/NavBar';
import { useEffect, useState } from "react";

function App() {
  const [user, setUser]= useState("internaute")

  const getUser=(usr)=>{
      setUser(usr)
      console.log(usr)
  }
  return (

    <BrowserRouter>
      <div className="App">
        <NavBar user={user}/>
          <Switch>
            <Route exact path='/Accueil' component={Acceuil} />
            <Route exact path='/' component={Acceuil} />
            <Route exact path='/RechercheClassique' component={RechercheClassique} />
            <Route exact path='/RechercheAvancee' component={RechercheAvancee} />
        </Switch>
      </div>
    </BrowserRouter>

  );
}

export default App;
