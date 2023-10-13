'use client'
import { signIn } from "next-auth/react";
import { useSearchParams } from "next/navigation";


export default function GoogleSignInBtn(){
    const searchParams = useSearchParams();
    let callback = searchParams.get('callback')

    if (!callback)
        callback = "/";
    
    const handleClick = () => {
        signIn("google", {callbackUrl: callback!});
    }


    return (
        <div className="login-method__item">
            <button className="login-method__button google-signin-btn" onClick={handleClick}/>
        </div>
    )
}