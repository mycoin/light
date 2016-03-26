import React from 'react';
import classNames from 'classnames';
import Card from 'material-ui/lib/card/card';
import CardActions from 'material-ui/lib/card/card-actions';
import CardHeader from 'material-ui/lib/card/card-header';
import CardTitle from 'material-ui/lib/card/card-title';
import CardText from 'material-ui/lib/card/card-text';
import RaisedButton from 'material-ui/lib/raised-button';
import Gravatar from '../Gravatar';
import Avatar from 'material-ui/lib/avatar';
import CommentBottomBanner from './CommentBottomBanner';
import moment from 'moment';
import _ from 'lodash';

var CommentNode = React.createClass({

    getInitialState: function() {
        return {
            comments: [],
            out_HasComment: this.props.comment.out_HasComment,
            upVoted: false,
            downVoted: false,
            spamed: false
        };
    },

    onChildDisplayToggle: function (ev) {
        if (this.props.comment.out_HasComment) {
            if (this.state.out_HasComment && this.state.out_HasComment.length) {
                this.setState({out_HasComment: null});
            } else {
                this.setState({out_HasComment: this.props.comment.out_HasComment});
            }
        }
        ev.preventDefault();
        ev.stopPropagation();
    },

    onAddComment: function(rid, text) {
        console.log('handleReply', rid, text);
        this.props.onAddComment(rid, text);
    },

    onSpam: function(rid) {
        console.log('onSpam', rid);
        this.setState({spamed: !this.state.spamed});
        this.props.onSpam(rid);
    },

    onUpVote: function(rid) {
        console.log('onUpVote', rid);
        this.setState({upVoted: !this.state.upVoted});
        this.props.onUpVote(rid);
    },

    onDownVote: function (rid) {
        console.log('onDownVote', rid);
        this.setState({downVoted: !this.state.downVoted});
        this.props.onDownVote(rid);
    },

    render: function () {
        if (!this.state.out_HasComment) this.state.out_HasComment = [];
        var classes = classNames({
            'has-comment': (this.props.comment.out_HasComment ? true : false),
            'open': (this.state.out_HasComment.length ? true : false),
            'closed': (this.state.out_HasComment ? false : true),
            'selected': (this.state.selected ? true : false)
        });

        let boundOnAddComment = this.onAddComment.bind(this, this.props.comment['@rid']);
        let boundOnSpam = this.onSpam.bind(this, this.props.comment['@rid']);
        let boundOnUpVote = this.onUpVote.bind(this, this.props.comment['@rid']);
        let boundOnDownVote = this.onDownVote.bind(this, this.props.comment['@rid']);

        var commentProps = {
            comment: {},
            onAddComment: this.props.onAddComment,
            onUpVote: this.props.onUpVote,
            onDownVote: this.props.onDownVote,
            onSpam: this.props.onSpam
        };

        var comments = this.state.out_HasComment.map(function(comment) {
            commentProps.comment = comment;
            commentProps.key = comment.commentId;
            return <CommentNode {...commentProps} />
        }.bind(this));

        var age = moment(this.props.comment.createDate).fromNow();

        return (
            <li ref="node">
                <div className={classes} onClick={this.onChildDisplayToggle}>
                    <div style={{display: 'inline-block', verticalAlign: 'top', paddingRight: '90px'}}>
                        <Gravatar md5={this.props.comment.gravatar} />
                        <span style={{display: 'inline-block', fontSize: 16}}>{this.props.comment.in_Create[0].userId + ' . ' + age}</span>
                    </div>
                    <div style={{display: 'block', fontSize: 15}}>{this.props.comment.content}</div>
                </div>
                <CommentBottomBanner
                    upVoted            = {this.state.upVoted}
                    onUpVote           = {boundOnUpVote}
                    downVoted          = {this.state.downVoted}
                    onDownVote         = {boundOnDownVote}
                    spamed             = {this.state.spamed}
                    onSpam             = {boundOnSpam}
                    onAddComment       = {boundOnAddComment}
                />
                <ul>
                    {comments}
                </ul>
            </li>
        );
    }
});

module.exports = CommentNode;

/*

 */