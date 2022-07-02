import {useAuth} from '../contexts/authContext';
import {Link} from 'react-router-dom';

function Navbar(props){
    const { user } = useAuth();

    return (
        <div id="Navbar" className="navbar navbar-expand-lg bg-dark">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/journal-app/">
                    <span>Journal App</span>
                </Link>
                <div className="collapse navbar-collapse">
                </div>

                {
                    (user == null)?   
                        <Link className="nav-link text-light" to="/journal-app/login">Login</Link>
                    :
                        <Link className="nav-link text-light" to="#">Logout</Link> 
                }
            </div>
        </div>
    );
}
export default Navbar;