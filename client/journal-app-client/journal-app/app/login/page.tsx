import React from 'react';
import Link from 'next/link';
import GoogleSignInBtn from '../../components/login/GoogleSignInBtn';


function LoginPage() {

    return (
        <div className="login-method animated-entry-top">
            <p className="paragraph">By continuing, you agree to the <Link href="/userAgreement">User Agreement</Link> for Journal App. </p>
            <div className="login-method__list-box">
                <GoogleSignInBtn />
            </div>
        </div>
    );
}
export default LoginPage;
