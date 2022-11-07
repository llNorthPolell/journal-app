import LineGraphMenu from '../../../pages/dashboard/widgetMenu/chart/line-graph-menu/line-graph-menu';

export default{
    title: "Forms/Widget Forms/Line Graph Menu",
    component: LineGraphMenu
}

const schemas = [{"records":["a","targetA"],"topic":"First Topic"},{"records":["b","targetB", "initB"],"topic":"Second Topic"}]

const close = () =>{
    console.log("Close Window")
}

const submit = output => {
    console.log("Final save to database: " + JSON.stringify(output));
}

export const LineGraphMenuFormStory = () => 
    <LineGraphMenu schemas={schemas} close={close} submit={submit} />