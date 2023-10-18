'use client'
import { signIn } from "next-auth/react";
import Head from "next/head";
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
        <>
            <Head>
                <link
                    rel="preload"
                    href="/btn_google_signin_dark_normal_web.png"
                    as="image"/>
                <link
                    rel="preload"
                    href="/btn_google_signin_dark_pressed_web.png"
                    as="image"/>
            </Head>
            <div className="login-method__item">
                <button className="login-method__button google-signin-btn" onClick={handleClick}/>
            </div>
        </>
    )
}