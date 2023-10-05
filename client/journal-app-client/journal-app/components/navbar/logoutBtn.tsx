'use client'

import {signOut} from "next-auth/react";


export default function LogoutBtn(){
    const handleLogout = async ()=>{
        signOut();
    };

    return (
        <>
            <button className="navbar__btn-link" onClick={handleLogout}>Logout</button>
        </>
    );
}