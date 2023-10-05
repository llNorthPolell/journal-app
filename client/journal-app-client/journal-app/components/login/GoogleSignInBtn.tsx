'use client'
import { signIn } from "next-auth/react";


export default function GoogleSignInBtn(){
    const handleClick = () => {
        signIn("google");
    }


    return (
        <div className="login-method__item">
            <button className="login-method__button google-signin-btn" onClick={handleClick}/>
        </div>
    )
}