import React, {useContext, useState, useEffect} from 'react';
import {auth,loginWithGoogle} from '../firebase'

const AuthContext = React.createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider ({children}){
    const [user,setUser] = useState();

    function login() {
        return loginWithGoogle();
    }


    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(user => {
            setUser(user);
        });
        return unsubscribe;
    },[]);


    const values = {
        auth,user,login
    }

    
    return (
        <AuthContext.Provider value={values}>
            {children}
        </AuthContext.Provider>
    )

}

