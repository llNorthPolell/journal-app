import {useAuth} from '../contexts/authContext';

function Navbar(props){
    const { user } = useAuth();

    return (
        <div id="Navbar" className="navbar navbar-expand-lg bg-dark">
            <div className="container-fluid">
                <a className="navbar-brand" href="/journal-app/">
                    <span>Journal App</span>
                </a>
                <div className="collapse navbar-collapse">
                </div>

                {
                    (user == null)?   
                        <a className="nav-link text-light" href="/journal-app/login">Login</a>
                    :
                        <a className="nav-link text-light" href="#">Logout</a> 
                }
            </div>
        </div>
    );
}
export default Navbar;