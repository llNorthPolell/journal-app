import {useAuth} from '../contexts/authContext';
import {Link,useNavigate} from 'react-router-dom';


function LoginPage(props){
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleClick = (e) => {
        e.preventDefault();
        login().then((result)=>{
            console.log("Login Successful!");
            navigate('/journal-app/');
        }).catch((error)=>{
            console.log(error.message);
        });

    }

    return (
        <div id="loginDiv">
            <p>By continuing, you agree to the <Link to="/journal-app/userAgreement">User Agreement</Link> for Journal App. </p>
            <button className="btn btn-primary" onClick={handleClick}>
                <img
                    className="card-img-top"
                    src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/signinwithgoogle.png?alt=media&token=b2c80e57-6f87-400d-8a44-d4c146bc2d83"
                    alt="Sign In with Google"
                />
            </button>
        </div>
    );
}
export default LoginPage;