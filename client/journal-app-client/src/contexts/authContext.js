import React, {useContext, useState, useEffect} from 'react';
import {loginToApp, logoutOfApp, getAuth} from '../facades/auth';

const AuthContext = React.createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider ({children}){
    const [user,setUser] = useState();
    const auth = getAuth();

    function login() {
        return loginToApp();
    }

    function logout(){
        logoutOfApp().then(()=>{
            setUser(null);
        });
    }

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(user => {
            setUser(user);
        });
        return unsubscribe;
    },[]);


    const values = {
        auth,user,login,logout
    }

    
    return (
        <AuthContext.Provider value={values}>
            {children}
        </AuthContext.Provider>
    )

}

