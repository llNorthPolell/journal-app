import React, {useContext,createContext, useState, useEffect} from 'react';
import {loginToApp, logoutOfApp, getAuth, User,Auth} from '../../facades/auth';

const AuthContext = createContext<AuthContextProps>({
    auth: null,
    user: null,
    login: ()=> {},
    logout:()=> {}
});

interface AuthContextProps{
    auth: Auth|null,
    user: User|null|undefined,
    login():void,
    logout():void
}

export interface AuthProviderProps {
    children: React.ReactNode
}

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider ({children} : AuthProviderProps){
    const [user,setUser]  = useState<User | null | undefined>();
    const auth = getAuth();

    function login() {
        loginToApp();
    }

    function logout(){
        logoutOfApp().then(()=>{
            setUser(null);
        });
    }

    auth.onAuthStateChanged((user : User|null|undefined) => {
        if (user)
            setUser(user);
    });

    const values = {
        auth,user,login,logout
    }

    
    return (
        <AuthContext.Provider value={values}>
            {children}
        </AuthContext.Provider>
    )

}

