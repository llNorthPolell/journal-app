import React, {useState} from 'react';
import Modal from 'react-bootstrap/Modal';
import WidgetMenuItems from './widget-menu-items.json';
import WidgetMenuForms from './widget-menu-forms';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons'

function WidgetMenuModal(props){
    const [show, setShow] = useState(false);
    const [widgetSearchInput, setWidgetSearchInput] = useState("");
    const [menu, setMenu] = useState("");

    function reset(){
        setWidgetSearchInput("");
        setMenu("");
    }


    const handleClose = ()=>{
        setShow(false);
        reset();
    }

    const handleShow = (e)=>{
        e.preventDefault();
        setShow(true);
    };


    const handleChangeWidgetSearchInput = e => {
        setWidgetSearchInput(e.target.value);
    }

    const clickMenuItem = value => {
        setMenu(value);
    }

    const handleClickBackButton = e =>{
        e.preventDefault();
        setMenu("");
    }


    return (
        <div>
            <button className="btn btn-outline-primary" onClick={handleShow}>Add Widget</button>
            <Modal id="widgetMenuModal" show={show} onHide={handleClose} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>New Widget</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    {
                        (menu==="")?
                        <div className="container">
                            <input id="widgetSearchBar" className="form-control" type="text" placeholder="search widgets" value={widgetSearchInput} onChange={handleChangeWidgetSearchInput}/>

                            <div id="widgetOptionsDiv">
                                    {
                                        WidgetMenuItems.map(widgetMenuItem =>
                                            <div className="row">
                                                <h5 className="widget-category-name">{widgetMenuItem.categoryName}</h5>
                                                
                                                {
                                                    widgetMenuItem.widgetOptions.map(widgetOption => 
                                                        <div className="card widget-card text-center">
                                                            <img
                                                                className="card-img-top"
                                                                src={widgetOption.img}
                                                                alt={widgetOption.displayName}
                                                            />
                                                            <div className="card-body">
                                                                <h5>
                                                                    <a 
                                                                        className="card-title widget-card-link stretched-link" 
                                                                        onClick={()=>clickMenuItem(widgetOption.key)} 

                                                                    >{widgetOption.displayName}</a>
                                                                </h5>
                                                            </div>
                                                        </div>
                                                    )
                                                }
                                            </div>
                                        )
                                    }
                                </div>
                            </div>
                        :
                        <div className="container">
                            <button className="btn btn-link"><span><FontAwesomeIcon icon={faArrowLeft} onClick={handleClickBackButton}/></span></button>
                            <WidgetMenuForms menu={menu}></WidgetMenuForms>
                        </div>
                    }
                </Modal.Body>
            </Modal>
        </div>
    );
}
export default WidgetMenuModal;