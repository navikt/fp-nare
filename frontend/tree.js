'use strict';

// ************** Generate the tree diagram	 *****************
var margin = {top: 20, right: 120, bottom: 20, left: 120},
    width = 960 - margin.right - margin.left,
    height = 500 - margin.top - margin.bottom;

var i = 0;

var tree = d3.layout.tree()
    .size([height, width]);

var diagonal = d3.svg.diagonal()
    .projection(function (d) {
        return [d.y, d.x];
    });

var svg = d3.select("body").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

// load the external data
d3.json("../output/test.json", function (error, root) {
    update(root);
});

var tip = d3.tip()
    .attr('class', 'd3-tip')
    .offset([-10, 0])
    .html(function(d) {
        return "<div> ID: <span style='color:lightblue;float: right'>"+ d.ruleIdentifcation+"</span></div> " +
            "<div> Beskrivelse: <span style='color:lightblue';float: right'>"+ d.ruleDescription+"</span></div> " +
            "<div> Evaluering: <span style='color:lightblue;float: right''>"+ d.reason+"</span></div>";
    })

svg.call(tip);

function update(source) {

    // Compute the new tree layout.
    var nodes = tree.nodes(source).reverse(),
        links = tree.links(nodes);

    // Normalize for fixed-depth.
    nodes.forEach(function (d) {
        d.y = d.depth * 180;
    });

    // Declare the nodes…
    var node = svg.selectAll("g.node")
        .data(nodes, function (d) {
            return d.id || (d.id = ++i);
        });

    // Enter the nodes.
    var nodeEnter = node.enter().append("g")
        .attr("class", "node")
        .attr("transform", function (d) {
            return "translate(" + d.y + "," + d.x + ")";
        });


    function getType(d) {
        switch (d.operator) {
            case "AND" :
                return "square";
            case "OR" :
                return "diamond";
            default:
                return "circle";
        }
    }

    nodeEnter.append("path")
        .attr("d", d3.svg.symbol()
            .size( function(d) { return 30*30 })
            .type( function(d) { return getType(d) }))
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .style("fill", function(d){ return d.resultat=="JA" ? "#206A42": "#88242B"});



    nodeEnter.append("text")
        .attr("x", function (d) {
            return d.children ? -20 : 20;
        })
        .attr("dy", ".35em")
        .attr("text-anchor", function (d) {
            return d.children  ? "end" : "start";
        })
        .text(function (d) {
            return d.children ? "" : d.reason;
        })
        .style("fill-opacity", 1);

    nodeEnter.append("text")
        .attr("dy", "0.35em")
        .attr("text-anchor", "middle")
        .text(function (d) {
            return d.resultat;
        })
        .on('mouseover', tip.show)
        .on('mouseout', tip.hide)
        .style("fill", "#FFF");

    // Declare the links…
    var link = svg.selectAll("path.link")
        .data(links, function (d) {
            return d.target.id;
        });

    // Enter the links.
    link.enter().insert("path", "g")
        .attr("class", "link")
        .attr("d", diagonal);


}