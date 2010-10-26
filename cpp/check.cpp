#include <iostream>
#include <fstream>
#include <map>
#include <vector>
#include <algorithm>
#include <list>
#include <queue>

// DEBUGGING:
// Bit 0 on: Information
// Bit 1 on: Move Information
// Bit 2 on: Print map
#define DEBUG 7

using namespace std;
char buffer[65536];

int dx[] = {-1, 0, 1, 0};
int dy[] = {0, 1, 0, -1};

char moveInto(char original, bool box, int &res){
    if (original == '.'){
        if (box){
            res--;
            return '*';
        } else {
            return '+';
        }
    }
    return box ? '$' : '@';
};

char moveOut(char original, int &res){
    if (original == '*'){
        res++;
        return '.';
    }
    if (original == '+'){
        return '.';
    }
    return ' ';
};

int makeMove(vector<vector<char> > &m, int &x, int &y, int move, int &res){
    int tx = x + dx[move];
    int ty = y + dy[move];

    if (DEBUG & 2){
        cout << x << ' ' << y << " to " << tx << ' ' << ty << endl;
    }

    if (tx < 0 || tx >= m.size()){
        return 3;
    }
    if (ty < 0 || ty >= m[tx].size()){
        return 3;
    }
    if (m[tx][ty] == '#'){
        return 1;
    }
    if (m[tx][ty] == '$' || m[tx][ty] == '*'){
        int rx = tx + dx[move];
        int ry = ty + dy[move];

        if (rx < 0 || rx >= m.size() || ry < 0 || ry >= m[rx].size()){
            return 3;
        }
        if (m[rx][ry] == '#' || m[rx][ry] == '$' || m[rx][ry] == '*'){
            return 2;
        }
        
        m[rx][ry] = moveInto(m[rx][ry], true, res);
        m[tx][ty] = moveOut(m[tx][ty], res);
    }

    m[tx][ty] = moveInto(m[tx][ty], false, res);
    m[x][y] = moveOut(m[x][y], res);
    x = tx;
    y = ty;

    if (DEBUG & 4)
    for(int i = 0; i < m.size(); i++){
        for (int j = 0; j < m[i].size(); j++){
            cout << m[i][j];
        }
        cout << endl;
    }
    return 0;
};

int main(int argc, char** argv){

    vector<vector<char> > m(0);

    char move;
    
    if (argc < 3){
        if (DEBUG & 1)
        cout << "Usage: " << argv[0] << " [map] [path]" << endl;
        return 1;
    }

    int remaining = 0;

    ifstream fin(argv[1]);
    ifstream sin(argv[2]);
    if (!fin || !sin){
        if (DEBUG & 1)
        cout << "Error loading file" << endl;
        return 1;
    }

    int x = 0;
    int y = 0;

    while(fin.getline(buffer, 65536)){
        int line = m.size();
        m.push_back(vector<char>(0));
        for(int i = 0; buffer[i]; i++){
            m[line].push_back(buffer[i]);
            if (m[line][i] == '@'){
                x = line;
                y = i;
            } else if (m[line][i] == '$'){
                remaining++;
            }
        }

    }
    map<int, int> translate;
    translate['u'] = 0;
    translate['r'] = 1;
    translate['d'] = 2;
    translate['l'] = 3;
    
    while(sin.good()){
        sin.get(move);
        if (move == 'u' || move == 'r' || move == 'd' || move == 'l')
        switch(makeMove(m, x, y, translate[move], remaining)){
            case 0:
                break;
            case 1:
                if (DEBUG & 1)
                cout << "Invalid move, cannot move over wall." << endl;
                return 1;
            case 2:
                if (DEBUG & 1)
                cout << "Invalid move, cannot move box over wall/box." << endl;
                return 1;
            case 3:
                if (DEBUG & 1)
                cout << "Out of bounds exception." << endl;
                return 1;
            default:
                if (DEBUG & 1)
                cout << "Unknown coutor" << endl;
                return 1;
        }
    }
    if (remaining){
        if (DEBUG & 1)
        cout << "The player did not win." << endl;
        if (DEBUG & 1)
        cout << "There were " << remaining << " boxes." << endl;
        return 1;
    } else {
        if (DEBUG & 1)
        cout << "Win." << endl;
        return 0;
    }
}

