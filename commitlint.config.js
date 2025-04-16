// commitlint.config.js
module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(\w+)\[#(\d+)\]:\s(.+)$/,
            headerCorrespondence: ['type', 'ticket', 'subject'],
        },
    },
    rules: {
        'type-enum': [2, 'always', ['feat', 'fix', 'chore', 'docs', 'style', 'refactor', 'test']],
        'subject-empty': [2, 'never'],
    },
};
