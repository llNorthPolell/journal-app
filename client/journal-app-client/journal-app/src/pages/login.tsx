import React from 'react';
import Link from 'next/link';
import {loginToApp} from '@/facades/auth';
import { useRouter } from 'next/router';


function LoginPage(){
    const router = useRouter();

    const handleClick = async (e :React.MouseEvent) => {
        try{
            await loginToApp();
        }
        catch(err){
            console.log(err);
        }
    }

    return (     
            <div className="login-method animated-entry-top">
                <p className="paragraph">By continuing, you agree to the <Link href="/userAgreement">User Agreement</Link> for Journal App. </p>
                <div className="login-method__list-box">
                    <div className="login-method__item">
                        <button className="login-method__button google-signin-btn" onClick={handleClick}>
                        </button>
                    </div>
                </div>
            </div>
    );
}
export default LoginPage;
