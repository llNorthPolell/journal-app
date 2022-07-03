import React, {useContext, useState, useEffect} from 'react';
import {auth,loginWithGoogle, signOut} from '../firebase'

const AuthContext = React.createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider ({children}){
    const [user,setUser] = useState();

    function login() {
        return loginWithGoogle();
    }

    function logout(){
        signOut().then(()=>{
            setUser(null);
        }).catch((err)=>{
            console.log("Error occurred while signing out... " + err.message);
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

