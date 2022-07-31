import React, {useEffect} from 'react';
import {useAuth} from '../contexts/authContext';
import {Link,useNavigate} from 'react-router-dom';


function LoginPage(props){
    const { login, auth } = useAuth();
    const navigate = useNavigate();


    useEffect(()=>{
        const unsubscribe = auth.onAuthStateChanged(authUser => {
            authUser? navigate("/") : navigate("/login");
        });
        return unsubscribe;
    },[]);



    const handleClick = (e) => {
        
        async function callLogin(){
            try {
                e.preventDefault();
                await login();
            } catch (err){
                console.log(err.message);
            }
        }
        callLogin();
    }

    return (
        <div id="loginDiv">
            <div className="card">
                <div className="card-body">
                    <p>By continuing, you agree to the <Link to="/userAgreement">User Agreement</Link> for Journal App. </p>
                    <button className="btn btn-primary" onClick={handleClick}>
                        <img
                            className="card-img-top signInOption"
                            src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Login%20Page%2Fsigninwithgoogle.png?alt=media&token=79d59d42-a095-41cc-b7af-82da99892c72"
                            alt="Sign In with Google"
                        />
                    </button>
                </div>
            </div>
        </div>
    );
}
export default LoginPage;