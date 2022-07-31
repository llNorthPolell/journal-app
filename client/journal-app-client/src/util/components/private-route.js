import React, { useState,useEffect } from 'react';
import {Navigate} from "react-router-dom";
import {useAuth} from "../../contexts/authContext";

function PrivateRoute({ children }){
    const { auth } = useAuth();
    const [userId,setUserId] = useState();
    const [isLoading, setIsLoading] = useState(true);

    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            setUserId(authUser?authUser.uid:null);
            setIsLoading(false);
        });
        return unsubscribe;
    },[]);

    if (isLoading) return "Loading..."
    else {
        return userId? children : <Navigate to={"/login"}/>;
    }

    
    
}
export default PrivateRoute;