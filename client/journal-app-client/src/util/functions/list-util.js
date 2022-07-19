const listUtil = (list,setList,action) => {
    switch(action.type){
        case "INSERT":
            setList([...list, action.payload]);
            break;
        case "GET":
            return list.find(item=>{return item.id===action.id});
        case "UPDATE":
            setList(list.map(item=>
                item.key === action.payload.key ? 
                    action.payload : item   
            ));
            break;
        case "DELETE":
            setList((action.id !== undefined)? 
                list.filter(item => item.id!==action.id):
                (action.key !== undefined)? list.filter(item=> item.key!==action.key):
                list.filter(item => item!==action.payload));
            break;
        case "CONTAINS":
            return list.some(item=> item == action.payload);
        case "SET":
            setList([...action.payload]);
            break;
        case "TRUNCATE":
            setList([]);
            break;
        default:
            return list;
    }
}
export default listUtil;