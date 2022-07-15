import {useAuth} from '../contexts/authContext';
import {useNavigate} from 'react-router-dom';


function LoginPage(props){
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleClick = (e) => {
        e.preventDefault();
        login().then((result)=>{
            console.log("Login Successful!");
            navigate('/journal-app');
        }).catch((error)=>{
            console.log(error.message);
        });

    }

    return (
        <div id="loginDiv">
            <button className="btn btn-primary" onClick={handleClick}>Sign in with Google</button>
        </div>
    );
}
export default LoginPage;